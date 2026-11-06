"""
预测充电费用
基于充电量、时段、时长预测充电费用
数据源: nvv2t.csv
"""
import pandas as pd
import numpy as np
from sklearn.ensemble import RandomForestRegressor
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error, r2_score
import joblib
import os

MODEL_PATH = os.path.join(os.path.dirname(__file__), 'model_charge_fee.pkl')


def train_model(data_path=None):
    """训练充电费用预测模型"""
    if data_path is None:
        data_path = os.path.join(os.path.dirname(__file__), '..', '..', '数据集', 'nvv2t.csv')

    df = pd.read_csv(data_path)

    # 特征
    features = ['kwhTotal', 'startTime', 'chargeTimeHrs']
    X = df[features].apply(pd.to_numeric, errors='coerce').fillna(0)
    X['is_peak'] = ((X['startTime'] >= 8) & (X['startTime'] <= 22)).astype(int)
    X = X.drop('startTime', axis=1)

    y = pd.to_numeric(df['charging_fees'], errors='coerce').fillna(0)

    # 去除异常值
    mask = (y >= 0) & (y < 500)
    X, y = X[mask], y[mask]

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

    model = RandomForestRegressor(n_estimators=100, max_depth=10, random_state=42, n_jobs=-1)
    model.fit(X_train, y_train)

    y_pred = model.predict(X_test)
    r2 = r2_score(y_test, y_pred)
    rmse = np.sqrt(mean_squared_error(y_test, y_pred))
    print(f"[Fee Model] R² = {r2:.4f}, RMSE = {rmse:.4f}")

    joblib.dump(model, MODEL_PATH)
    return model


def load_model():
    if os.path.exists(MODEL_PATH):
        return joblib.load(MODEL_PATH)
    else:
        print("[Fee Model] 模型文件不存在，开始训练...")
        return train_model()


def predict_fee(data):
    """
    预测充电费用
    data: dict with keys: kwh, hour, duration
    """
    model = load_model()

    kwh = data.get('kwh', 7)
    hour = data.get('hour', 12)
    duration = data.get('duration', 2)

    is_peak = 1 if (8 <= hour <= 22) else 0
    features = np.array([[kwh, duration, is_peak]])
    predicted_fee = model.predict(features)[0]
    predicted_fee = max(0, predicted_fee)

    return {
        'predicted_fee': round(predicted_fee, 2),
        'unit': '元',
        'is_peak_hours': bool(is_peak),
        'rate_type': '峰时' if is_peak else '谷时'
    }


if __name__ == '__main__':
    train_model()
    test_data = {'kwh': 10, 'hour': 14, 'duration': 2}
    result = predict_fee(test_data)
    print(f"测试预测: {result}")
