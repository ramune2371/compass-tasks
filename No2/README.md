# 課題2

## 注意点
- 使用言語はJava11.0.2を使用しています
- Eclipse および Mavenを使用しています
- package済みの実行ファイルは`target/server-jar-with-dependencies.jar`です

## 課題外の仕様説明
- `GET`method以外へは405エラーを返却します
- 受け付けるファイル拡張子は`html`,`png`,`jpeg`のみになっています。
	- 上記以外の場合は404を返却

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
## 動作確認方法
- 実行後、chrome等で以下のURLへアクセス(2~4については動作確認ファイルを使用した場合です。)
	1. localhost:<起動時に指定したport>/ping <- `/ping`へアクセスし`/pong`が返却されることの確認
	2. localhost:<起動時に指定したport>/test.html <- `/ping`以外へのアクセス時の確認(html)
	3. localhost:<起動時に指定したport>/test.png <- `/ping`以外へのアクセス時の確認(png)
	4. localhost:<起動時に指定したport>/test.jpeg <- `/ping`以外へのアクセス時の確認(jpeg)
	5. localhost:<起動時に指定したport>/hogehoge <- `/ping`以外へのアクセス時の確認(拡張子なし 404 error)
	6. localhost:<起動時に指定したport>/hogehoge.pdf <- `/ping`以外へのアクセス時の確認(拡張子あり 404 error)

## build方法
- `No2`ディレクトリにて以下を実行
```
./mvwn clean compile package
```

## デバッグログ有効での起動方法
- `DEBUG_ENABLED`環境変数を`true`で設定し、jarファイルを実行
