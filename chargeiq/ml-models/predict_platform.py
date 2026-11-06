"""
预测充电平台选用
分类问题: 根据用户行为特征预测使用的充电平台(android/ios)
数据源: nvv2t.csv (platform字段)
"""
import pandas as pd
import numpy as np
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, classification_report
import joblib
import os

MODEL_PATH = os.path.join(os.path.dirname(__file__), 'model_platform.pkl')


def train_model(data_path=None):
    """训练平台选用预测模型"""
    if data_path is None:
        data_path = os.path.join(os.path.dirname(__file__), '..', '..', '数据集', 'nvv2t.csv')

    df = pd.read_csv(data_path)

    # 特征工程
    features = ['kwhTotal', 'startTime', 'chargeTimeHrs', 'weekday']
    X = pd.DataFrame()
    for f in features:
        X[f] = pd.to_numeric(df[f], errors='coerce').fillna(0)

    X['is_weekend'] = ((X['weekday'] == 'Sat') | (X['weekday'] == 'Sun')).astype(int)
    X['hour_sin'] = np.sin(2 * np.pi * X['startTime'] / 24)
    X['hour_cos'] = np.cos(2 * np.pi * X['startTime'] / 24)
    X = X.drop(['startTime', 'weekday'], axis=1)

    # 目标: platform (android -> 1, ios -> 0)
    y = df['platform'].map({'android': 1, 'ios': 0}).fillna(1).astype(int)

    mask = X.sum(axis=1) != 0
    X, y = X[mask], y[mask]

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42, stratify=y)

    model = RandomForestClassifier(n_estimators=100, max_depth=8, random_state=42, n_jobs=-1)
    model.fit(X_train, y_train)

    y_pred = model.predict(X_test)
    acc = accuracy_score(y_test, y_pred)
    print(f"[Platform Model] Accuracy = {acc:.4f}")
    print(classification_report(y_test, y_pred, target_names=['iOS', 'Android']))

    joblib.dump(model, MODEL_PATH)
    return model


def load_model():
    if os.path.exists(MODEL_PATH):
        return joblib.load(MODEL_PATH)
    else:
        print("[Platform Model] 模型文件不存在，开始训练...")
        return train_model()


def predict_platform(data):
    """
    预测充电平台选用
    data: dict with keys: kwh, hour, weekday
    返回: 预测的平台和置信度
    """
    model = load_model()

    kwh = data.get('kwh', 7)
    hour = data.get('hour', 12)
    weekday = data.get('weekday', 3)

    is_weekend = 1 if weekday in [5, 6] else 0
    features = np.array([[
        kwh, 2.0, is_weekend,
        np.sin(2*np.pi*hour/24),
        np.cos(2*np.pi*hour/24)
    ]])

    proba = model.predict_proba(features)[0]
    predicted_class = model.predict(features)[0]
    platform = 'Android' if predicted_class == 1 else 'iOS'
    confidence = max(proba)

    return {
        'predicted_platform': platform,
        'confidence': round(confidence * 100, 1),
        'unit': '%',
        'probabilities': {
            'Android': round(proba[1] * 100, 1),
            'iOS': round(proba[0] * 100, 1)
        }
    }


if __name__ == '__main__':
    train_model()
    test_data = {'kwh': 9.74, 'hour': 17, 'weekday': 2}
    result = predict_platform(test_data)
    print(f"测试预测: {result}")
