# 課題1

## 注意点
- 各リソースの展開先がnamespace=defaultになっています。
- nginx→redis1/2へのProxyPassが内部DNSを用いているため、namespaceを変更する際は./nginx/config/nginx.conf内の15,16行目を変更する必要があります。
- redis1/2のDBファイルを永続化/共通化していません。

## リソース展開方法
```
kustomize build | kubectl apply -f -
```


## リソース削除方法
```
kustomize build | kubectl delete -f -
```

## 動作確認方法
### Nginx Serviceへポートフォワード
```
kubectl port-forward service/nginx-service 8888:6379 &
```

### redis-cliを用いてリクエストの送信
```
for I in `seq 1 100`; do redis-cli -p 8888 ping; done
```

### 結果の取得
```
kubectl logs `kubectl get pods --no-headers -o custom-columns=":metadata.name" | grep nginx` | grep proxy | sed -e 's/.*connected//g' | sort | uniq -c
```
### Redis1/2のServic IP確認
```
kubectl get svc
```
