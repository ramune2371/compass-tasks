# 課題2

## 注意点
- 使用言語はJava11.0.2を使用しています
- Eclipse および Mavenを使用しています
- package済みの実行ファイルは`target/server-jar-with-dependencies.jar`です

## 実行方法
- server-jar-with-dependencies.jarを任意のディレクトリに配置し、以下を実行
```
java -jar server-jar-with-dependencies.jar <port>
※ <port>には任意のport番号を入力してください。指定しない場合は18080で起動します。
例) 8080を使用して起動したい場合
java -jar server-jar-with-dependencies.jar 8080
```
- プログラムを終了したい場合は`Ctl + C`で終了してください
- `No`2ディレクトリと`No2/target`ディレクトリに以下の動作確認用サンプルファイルを配置しています
	- test.html (htmlファイル動作確認用)
	- test.png	(pngファイル動作確認用)
	- test.jpeg (jpegファイル動作確認用)
- 上記ファイルを使用して動作確認をする際は以下のコマンドを使用してください。
  - `No2`配下のファイルと`No2/target`配下のファイルは別の内容となっています。
```
No2ディレクトリにいる状態で以下を実行
java -jar target/server-jar-with-dependencies.jar <port>

No2/targetディレクトリにいる状態で以下を実行
java -jar server-jar-with-dependencies.jar <port>
```

## build方法
- `No2`ディレクトリにて以下を実行
```
./mvwn clean compile package
```

## デバッグログ有効での起動方法
- `DEBUG_ENABLED`環境変数を`true`で設定し、jarファイルを実行
