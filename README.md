## 共通

1. 任意の場所にリポジトリをクローンします

    ~~~
    $ cd path/to/imsjapan
    $ git clone https://github.com/IMSJapan/clicker-java-sample.git
    $ cd ../imsglobal
    $ git clone https://github.com/IMSGlobal/caliper-java.git
    ~~~

1. caliper-java について、利用するバージョンのタグをチェックアウトします

    ~~~
    $ cd path/to/caliper-java
    $ git checkout 1.0.0
    ~~~

## IntelliJ IDEA を用いた環境構築

### 環境

 * IntelliJ IDEA

### 手順

1. IntelliJ IDEA を起動し、プロジェクトをオープンします
    * Welcome で "Open" を選択し、clicker をクローンしたディレクトリを選択
1. "Error Loading Project" として caliper-java が見つからないエラーが表示された場合は設定を修正します
    * [File] - [Project tructure] から Project Structure ダイアログを表示
    * "Modules": [+] - [Import Module] を選択し、クローンした caliper-java のディレクトリを選択
    * ウィザードに沿ってインポートします（基本的には [Next] だけで大丈夫）
1. [Run] - [Run Application] で実行すると、起動します

## eclipse を用いた環境構築

### 環境

 * eclipse
 * Sprintg Tool

### 手順

1. eclipse を起動し、新規のワークスペースを作成します
1. クローンした clicker をインポートします
    * [File] - [Import] より、インポートウィザードを起動します
    * Select: "Maven" - "Existing Maven Project" を選択して [Next >]
    * Maven Projects: "Root Directory" にクローンしたディレクトリを指定し、"Projects" で org.imsjapan.sample:clicker:0.0.1-SNAPSHOT.jar が選択されていることを確認し [Finish]
1. 同様にクローンした caliper-java をインポートします
    * [File] - [Import] より、インポートウィザードを起動します
    * Select: "Maven" - "Existing Maven Project" を選択して [Next >]
    * Maven Projects: "Root Directory" にクローンしたディレクトリを指定し、"Projects" で org.imsglobal.caliper:caliper-java:1.0.0.jar が選択されていることを確認し [Finish]
1. clicker から caliper-java を利用するよう依存設定をします
    * Navigator で "clicker" を右クリックし [Properties] を選択
    * "Java Build Path" - "Projects" で [Add] をクリックし、caliper-java を選択し [OK]
    * 設定ができたら [Apply and Close]
1. 起動設定を作成します
    * [Run] - [Run Configurations] よりダイアログを表示します
    * "Spring Boot App" をダブルクリックし、設定を作成します
    * Name: Clicker
    * Project: clicker
    * Main type: org.imsjapan.sample.clicker.Application
    * 設定できたら、画面下部の [Apply] をクリックし、[Run] で実行します

## Canvas での LTI 設定

1. 講師権限でコースを開きます
1. コース内メニュー [設定] - [アプリ] - [アプリ設定の表示] とクリックします
1. "外部アプリ" の一覧において、 [+アプリ] をクリックします
1. "アプリの追加" ダイアログで LTI アプリの追加設定をおこないます
    * 設定のタイプ: XML を貼り付け
    * 名前: アプリ名
    * コンシューマ鍵: key
    * 共有シークレット: secret
    * XML設定: https://canvas.instructure.com/doc/api/file.tools_xml.html を参考に XML を作成し貼り付けます
        * <blti:launch_url>: ローンチURL
        * <blti:title>: コース内メニューに表示されるアプリ名
        * <blti:description>: アプリの説明
    例）コース内メニューに表示する場合
    ~~~
    <?xml version="1.0" encoding="UTF-8"?>
    <cartridge_basiclti_link xmlns="http://www.imsglobal.org/xsd/imslticc_v1p0"
        xmlns:blti = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0"
        xmlns:lticm ="http://www.imsglobal.org/xsd/imslticm_v1p0"
        xmlns:lticp ="http://www.imsglobal.org/xsd/imslticp_v1p0"
        xmlns:xsi = "http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation = "http://www.imsglobal.org/xsd/imslticc_v1p0 http://www.imsglobal.org/xsd/lti/ltiv1p0/imslticc_v1p0.xsd
        http://www.imsglobal.org/xsd/imsbasiclti_v1p0 http://www.imsglobal.org/xsd/lti/ltiv1p0/imsbasiclti_v1p0.xsd
        http://www.imsglobal.org/xsd/imslticm_v1p0 http://www.imsglobal.org/xsd/lti/ltiv1p0/imslticm_v1p0.xsd
        http://www.imsglobal.org/xsd/imslticp_v1p0 http://www.imsglobal.org/xsd/lti/ltiv1p0/imslticp_v1p0.xsd">
        <blti:launch_url>http://localhost:8080/launch</blti:launch_url>
        <blti:title>Clicker</blti:title>
        <blti:description>Provides a clicker tool</blti:description>
        <blti:extensions platform="canvas.instructure.com">
          <lticm:property name="privacy_level">public</lticm:property>
          <lticm:options name="course_navigation">
            <lticm:property name="enabled">true</lticm:property>
          </lticm:options>
        </blti:extensions>
    </cartridge_basiclti_link>
    ~~~
    * 設定できたら [提出] をクリックします