"""
预测充电时间
基于充电量(kwhTotal)和当前SOC预测所需充电时长
数据源: nvv2t.csv + dsv13r1.csv
"""
import pandas as pd
import numpy as np
from sklearn.ensemble import RandomForestRegressor
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error, r2_score
import joblib
import os

MODEL_PATH = os.path.join(os.path.dirname(__file__), 'model_charge_time.pkl')


def train_model(data_path=None):
    """训练充电时间预测模型"""
    if data_path is None:
        data_path = os.path.join(os.path.dirname(__file__), '..', '..', '数据集', 'nvv2t.csv')

    df = pd.read_csv(data_path)

    # 特征: kwhTotal, startTime, weekday
    features = ['kwhTotal', 'startTime']
    X = df[features].apply(pd.to_numeric, errors='coerce').fillna(0)
    # 添加衍生特征
    X['hour_sin'] = np.sin(2 * np.pi * X['startTime'] / 24)
    X['hour_cos'] = np.cos(2 * np.pi * X['startTime'] / 24)
    X = X.drop('startTime', axis=1)

    y = pd.to_numeric(df['chargeTimeHrs'], errors='coerce').fillna(0)

    # 去除异常值
    mask = (y > 0) & (y < 12)
    X, y = X[mask], y[mask]

    # 划分
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

    # 训练
    model = RandomForestRegressor(n_estimators=100, max_depth=8, random_state=42, n_jobs=-1)
    model.fit(X_train, y_train)

    # 评估
    y_pred = model.predict(X_test)
    r2 = r2_score(y_test, y_pred)
    rmse = np.sqrt(mean_squared_error(y_test, y_pred))
    print(f"[Charge Time Model] R² = {r2:.4f}, RMSE = {rmse:.4f}")

    joblib.dump(model, MODEL_PATH)
    return model


def load_model():
    if os.path.exists(MODEL_PATH):
        return joblib.load(MODEL_PATH)
    else:
        print("[Charge Time Model] 模型文件不存在，开始训练...")
        return train_model()


def predict_time(data):
    """
    预测充电时间
    data: dict with keys: kwh (充电量), current_soc, target_soc
    """
    model = load_model()

    kwh = data.get('kwh', 7)
    current_soc = data.get('current_soc', 20)
    target_soc = data.get('target_soc', 80)

    # 估算所需充电量
    needed_kwh = kwh * (target_soc - current_soc) / 100
    needed_kwh = max(0.1, needed_kwh)

    # 使用模型预测 (以中午12点为例)
    features = np.array([[needed_kwh, np.sin(2*np.pi*12/24), np.cos(2*np.pi*12/24)]])
    predicted_hours = model.predict(features)[0]
    predicted_hours = max(0.1, predicted_hours)

    return {
        'predicted_time_hours': round(predicted_hours, 2),
        'predicted_time_minutes': round(predicted_hours * 60, 0),
        'needed_kwh': round(needed_kwh, 2),
        'unit': '小时'
    }


if __name__ == '__main__':
    train_model()
    test_data = {'kwh': 7.78, 'current_soc': 20, 'target_soc': 80}
    result = predict_time(test_data)
    print(f"测试预测: {result}")
