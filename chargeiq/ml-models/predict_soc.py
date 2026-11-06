"""
预测剩余电量(SOC)
使用电池状态特征(电压、电流、温度)训练回归模型预测SOC
数据源: dsv13r1.csv
"""
import pandas as pd
import numpy as np
from sklearn.ensemble import RandomForestRegressor
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error, r2_score
import joblib
import os

MODEL_PATH = os.path.join(os.path.dirname(__file__), 'model_soc.pkl')


def train_model(data_path=None):
    """训练SOC预测模型"""
    if data_path is None:
        data_path = os.path.join(os.path.dirname(__file__), '..', '..', '数据集', 'dsv13r1.csv')

    # 加载数据 - 注意此CSV无header
    columns = ['id', 'record_time', 'soc', 'pack_voltage', 'charge_current',
               'max_cell_voltage', 'min_cell_voltage', 'max_temperature',
               'min_temperature', 'available_energy', 'available_capacity']
    df = pd.read_csv(data_path, names=columns)

    # 特征选择
    features = ['pack_voltage', 'charge_current', 'max_cell_voltage',
                'min_cell_voltage', 'max_temperature', 'min_temperature']
    X = df[features].apply(pd.to_numeric, errors='coerce').fillna(0)
    y = pd.to_numeric(df['soc'], errors='coerce').fillna(0)

    # 去除0值行
    mask = (X.sum(axis=1) != 0) & (y != 0)
    X, y = X[mask], y[mask]

    # 划分训练集和测试集
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

    # 训练随机森林回归模型
    model = RandomForestRegressor(n_estimators=100, max_depth=10, random_state=42, n_jobs=-1)
    model.fit(X_train, y_train)

    # 评估
    y_pred = model.predict(X_test)
    r2 = r2_score(y_test, y_pred)
    rmse = np.sqrt(mean_squared_error(y_test, y_pred))
    print(f"[SOC Model] R² = {r2:.4f}, RMSE = {rmse:.4f}")

    # 保存模型
    joblib.dump(model, MODEL_PATH)
    return model


def load_model():
    """加载已训练的模型"""
    if os.path.exists(MODEL_PATH):
        return joblib.load(MODEL_PATH)
    else:
        print("[SOC Model] 模型文件不存在，开始训练...")
        return train_model()


def predict_soc(data):
    """
    预测剩余电量
    data: dict with keys: voltage, current, temp, soc (当前值)
    返回: dict with predicted_soc
    """
    model = load_model()

    # 构建特征向量
    features = np.array([[
        data.get('voltage', 320),
        data.get('current', -20),
        data.get('voltage', 320) * 0.011,   # max_cell ~= pack_voltage / num_cells
        data.get('voltage', 320) * 0.011,
        data.get('temp', 30),
        data.get('temp', 30) - 1
    ]])

    predicted = model.predict(features)[0]
    predicted = max(0, min(100, predicted))  # 限制在0-100

    return {
        'predicted_soc': round(predicted, 2),
        'unit': '%',
        'current_soc': data.get('soc', 0)
    }


if __name__ == '__main__':
    # 训练模型
    train_model()
    # 测试预测
    test_data = {'voltage': 323.5, 'current': -22.5, 'temp': 32, 'soc': 15.6}
    result = predict_soc(test_data)
    print(f"测试预测: {result}")
