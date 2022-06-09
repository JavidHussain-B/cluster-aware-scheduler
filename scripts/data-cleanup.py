import requests
import os


def main(dict):
    access_token = get_access_token()
    call_data_cleanup_api(access_token=access_token)
    return {'message': 'Success'}


# 0 9 * * *
# Above is the cron expression
def call_data_cleanup_api(access_token):
    print("Calling data cleanup api")
    url = 'https://spring.cloud.ibm.com/data-cleanup'
    payload = ''
    headers = {
        'Authorization': 'Bearer ' + access_token
    }
    response = requests.request('POST', url, data=payload, headers=headers)
    print(response.text)


def get_access_token():
    headers = {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Accept': 'application/json',
    }
    api_key = os.environ['__OW_IAM_NAMESPACE_API_KEY']
    data = {
        'grant_type': 'urn:ibm:params:oauth:grant-type:apikey',
        'apikey': api_key
    }
    iam_url = os.environ['__OW_IAM_API_URL']
    response = requests.post(iam_url, headers=headers, data=data)
    return response.json()['access_token']