import requests
import json

# 测试登录接口
def test_login():
    url = "http://localhost:8081/employee/login"
    headers = {
        "Content-Type": "application/json"
    }
    # 测试正确密码
    data = {
        "username": "shop_admin2",
        "password": "123"
    }
    response = requests.post(url, headers=headers, data=json.dumps(data))
    print("正确密码测试：")
    print("状态码：", response.status_code)
    print("响应内容：", response.json())
    print()
    
    # 测试错误密码
    data = {
        "username": "shop_admin2",
        "password": "123456"
    }
    response = requests.post(url, headers=headers, data=json.dumps(data))
    print("错误密码测试：")
    print("状态码：", response.status_code)
    print("响应内容：", response.json())

if __name__ == "__main__":
    test_login()