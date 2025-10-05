#!/bin/bash

# 1. 만약 기존에 billow-secrets가 있다면 깔끔하게 삭제합니다.
echo "Deleting existing secret..."
kubectl delete secret billow-secrets --ignore-not-found=true

# 2. 모든 환경변수를 포함하여 새로운 Secret을 생성합니다.
echo "Creating new secret..."
kubectl create secret generic billow-secrets \
  --from-literal=JWT_SECRET_KEY='ajf48fjAFJdfjasf8302rJFjajs98fjsA23q' \
  --from-literal=KAKAO_CLIENT_ID='7ec4f6a69b4d7748dbead0c3b24f4bd4' \
  --from-literal=KAKAO_REST_API_KEY='7ec4f6a69b4d7748dbead0c3b24f4bd4' \
  --from-literal=KAKAO_NATIVE_APP_KEY='b70b066e6c735236882c7e05c089e3f6' \
  --from-literal=KAKAO_CLIENT_SECRET='098cdbb27da0363dfb4cd48a67c75145' \
  --from-literal=KAKAO_REDIRECT_URI='http://210-178-1-111.nip.io/login/oauth2/code/kakao' \
  --from-literal=DB_URL='jdbc:mysql://mysql-service:3306/billow' \
  --from-literal=DB_USERNAME='root' \
  --from-literal=DB_PASSWORD='1234' \
  --from-literal=NCP_APIGW_URL='https://uttw39i0jz.apigw.ntruss.com/custom/v1/45714/b400aeb62e214c205fbde034a00d65efa0ae13e5d814577d1885b30f7ed421ad/general' \
  --from-literal=NCP_SECRET_KEY='YVhIVldYSW55WEZLdmRXTVVnTmJCUEttS1pIU1Z3THM=' \
  --from-literal=SPRING_PROFILES_ACTIVE='prod'

echo "Script finished."