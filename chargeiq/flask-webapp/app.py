"""
新能源充电桩大数据项目 - Flask Web 主应用 (独立版)
提供数据可视化展示和机器学习预测功能
无需外部ml_models模块，预测逻辑内置
"""
from flask import Flask, render_template, jsonify, request
import os, sys, math, random

app = Flask(__name__)

# ==================== 数据库配置 ====================
DB_CONFIG = {
    'host': 'localhost',
    'port': 3306,
    'user': 'root',
    'password': 'root',
    'database': 'enger',
    'charset': 'utf8'
}


def query_db(sql, args=None):
    """执行SQL查询并返回结果，带容错"""
    try:
        import pymysql
        conn = pymysql.connect(**DB_CONFIG)
        cursor = conn.cursor()
        cursor.execute(sql, args)
        columns = [col[0] for col in cursor.description]
        rows = cursor.fetchall()
        conn.close()
        return [dict(zip(columns, row)) for row in rows]
    except Exception as e:
        print(f"[DB Error] {e}")
        return []


# ==================== 页面路由 ====================
@app.route('/')
def index():
    return render_template('index.html')


@app.route('/dashboard')
def dashboard():
    return render_template('dashboard.html')


@app.route('/monitor')
def monitor():
    """实时监控大屏"""
    return render_template('monitor.html')


@app.route('/predict')
def predict_page():
    return render_template('predict.html')


# ==================== 数据可视化API (7个) ====================
@app.route('/api/v1/voltage_current')
def api_v1():
    return jsonify(query_db(
        "SELECT record_time, avg_pack_voltage, avg_charge_current FROM t_enger1 ORDER BY record_time"))

@app.route('/api/v2/cell_voltage')
def api_v2():
    return jsonify(query_db("SELECT voltage_type, voltage_value FROM t_enger2"))

@app.route('/api/v3/hourly_soc')
def api_v3():
    return jsonify(query_db("SELECT record_hour, avg_soc FROM t_enger3 ORDER BY record_hour"))

@app.route('/api/v4/charge_frequency')
def api_v4():
    return jsonify(query_db(
        "SELECT charge_hour, charge_count, frequency_ratio FROM t_enger4 ORDER BY charge_hour"))

@app.route('/api/v5/charge_time_dist')
def api_v5():
    return jsonify(query_db("SELECT time_range, session_count FROM t_enger5"))

@app.route('/api/v6/charge_rate')
def api_v6():
    return jsonify(query_db("SELECT record_hour, avg_charge_rate FROM t_enger6 ORDER BY record_hour"))

@app.route('/api/v7/performance_index')
def api_v7():
    return jsonify(query_db(
        "SELECT station_id, total_kwh, avg_charge_time, total_fees, session_count FROM t_enger7"))


# ==================== ML预测逻辑(内置) ====================
# 优先尝试加载joblib模型，失败则使用启发式算法

def _predict_soc_heuristic(data):
    """SOC预测-启发式: 基于电压和电流估算"""
    voltage = float(data.get('voltage', 320))
    current = float(data.get('current', -20))
    temp = float(data.get('temp', 30))
    current_soc = float(data.get('soc', 15))
    # 电压越高+充电电流越大 → SOC增长越快
    delta = (voltage / 400) * abs(current) * 0.05 * (1 + (temp - 25) * 0.01)
    predicted = min(100, max(0, current_soc + delta * 10))
    return {'predicted_soc': round(predicted, 2), 'unit': '%', 'method': 'heuristic'}


def _predict_time_heuristic(data):
    """充电时间预测-启发式"""
    kwh = float(data.get('kwh', 7))
    current_soc = float(data.get('current_soc', 20))
    target_soc = float(data.get('target_soc', 80))
    needed = kwh * (target_soc - current_soc) / 100
    hours = max(0.1, needed / 7)  # 假设平均7kW充电功率
    return {'predicted_time_hours': round(hours, 2),
            'predicted_time_minutes': round(hours * 60, 0),
            'needed_kwh': round(max(0.1, needed), 2),
            'unit': '小时', 'method': 'heuristic'}


def _predict_fee_heuristic(data):
    """充电费用预测-启发式"""
    kwh = float(data.get('kwh', 7))
    hour = int(data.get('hour', 12))
    duration = float(data.get('duration', 2))
    is_peak = 8 <= hour <= 22
    rate = 0.8 if is_peak else 0.4  # 峰时0.8元/kWh, 谷时0.4
    fee = kwh * rate + duration * (0.3 if is_peak else 0.1)
    return {'predicted_fee': round(fee, 2), 'unit': '元',
            'is_peak_hours': is_peak, 'rate_type': '峰时' if is_peak else '谷时', 'method': 'heuristic'}


def _predict_platform_heuristic(data):
    """平台选用预测-启发式: 基于时段和充电量"""
    hour = int(data.get('hour', 12))
    kwh = float(data.get('kwh', 7))
    # 白天+大电量偏向Android, 夜间+小电量偏向iOS (简化模型)
    android_score = 50 + (10 if 8 <= hour <= 20 else -10) + (5 if kwh > 8 else -5)
    android_score = max(10, min(90, android_score))
    ios_score = 100 - android_score
    platform = 'Android' if android_score > 50 else 'iOS'
    return {'predicted_platform': platform,
            'confidence': round(max(android_score, ios_score), 1),
            'probabilities': {'Android': round(android_score, 1), 'iOS': round(ios_score, 1)},
            'unit': '%', 'method': 'heuristic'}


# 尝试加载真实ML模型
_ml_models = {}
_ml_path = os.path.join(os.path.dirname(__file__), '..', 'ml-models')
if os.path.isdir(_ml_path):
    sys.path.insert(0, _ml_path)

for _name, _func, _heuristic in [
    ('soc', 'predict_soc', _predict_soc_heuristic),
    ('time', 'predict_time', _predict_time_heuristic),
    ('fee', 'predict_fee', _predict_fee_heuristic),
    ('platform', 'predict_platform', _predict_platform_heuristic),
]:
    try:
        _mod = __import__(f'predict_{_name}', fromlist=[_func])
        _ml_models[_name] = getattr(_mod, _func)
        print(f"[ML] Loaded predict_{_name} from ml-models/")
    except Exception as e:
        _ml_models[_name] = _heuristic
        print(f"[ML] Using heuristic for {_name} ({e})")


# ==================== 预测API ====================
@app.route('/api/predict/soc', methods=['POST'])
def api_predict_soc():
    data = request.get_json() or {}
    return jsonify(_ml_models['soc'](data))

@app.route('/api/predict/charge_time', methods=['POST'])
def api_predict_charge_time():
    data = request.get_json() or {}
    return jsonify(_ml_models['time'](data))

@app.route('/api/predict/fee', methods=['POST'])
def api_predict_fee():
    data = request.get_json() or {}
    return jsonify(_ml_models['fee'](data))

@app.route('/api/predict/platform', methods=['POST'])
def api_predict_platform():
    data = request.get_json() or {}
    return jsonify(_ml_models['platform'](data))


if __name__ == '__main__':
    print("=" * 60)
    print(" 新能源充电桩大数据平台")
    print(" 访问 http://localhost:5000")
    print("=" * 60)
    app.run(host='0.0.0.0', port=5000, debug=True)
