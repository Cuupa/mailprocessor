# mailprocessor

## About this project
This is a project to automate archiving of documents. 
Currently, it supports fetching from an email account or working through documents on a folder, being a local folder or on a webdav share.

This project is provided via the MIT-licence, which is free of charge. But if you want to support me, you can spend me a beer or a coffee.
If you want to participate, feel free to create pull requests, fork this project or hit me up with suggestions.

THIS IS A WORK IN PROGRESS and done in my spare time.

## How it works
This project uses Camunda BPMN for orchestration (https://camunda.org) and DMN for decision tables.
The project is a spring boot project, so it works as a jar file as well as deployed to a tomcat/websphere/glassfish/etc application server.

## Configuration
Below there's a sample configuration file in plain old json. Each section can be deactivated by simply deleting the property or using the enabled switch.
The path to the configuration is specified in the application.yml.

### Example configuration file
    [{
      "username": "username",
      "locale": "en_EN",
      "reload": true,
      "emailproperties": {
        "servername": "imap.mail.com",
        "port": 993,
        "username": "username@mail.com",
        "password": "password",
        "protocol": "imap",
        "labels": ["inbox", "label1", "label2"],
        "markasread": true,
        "enabled": false
      },
      "scanproperties": {
        "path": "\\path\\to\\documents",
        "port": 0,
        "username": "username",
        "password": "password",
        "scannerprefix": ["scannerprefix"],
        "filetypes": ["pdf"],
        "enabled": true
      },
      "archiveproperties": {
        "path": "https://webdav.network.share",
        "port": 443,
        "username": "username",
        "password": "password"
      },
      "reminderproperties": {
        "botname": "botname",
        "token": "token",
        "chatId": "chatId",
        "url": "http://url.call",
        "enabled": false
      }
    }]

### Explanation of the sections
#### General properties
- username: the user for which this entry is for. Mandatory but can be anything you seem fit
- locale: the locale the topics should be translated to. Can be left out or deleted if there is no translation to be done
- reload: should the configuration file be reloaded periodically

#### emailproperties
This section is about the configuration of fetching emails
- servername: the servername of your mailprovider
- port: the port the mailserver provides
- username: the username for your mailaccount
- password: the password for your mailaccount. I strongly encurage you to use a one-time-password like gmail offers
- protocol: the protocol which you use to talk to your mailserver. IMAP (recommended) or POP3
- labels: the labels or folder you want to process. Use "*" to process all labels and sublabels
- markasread: mark the processed mails as "read"
- enabled: the flag to enable or disable this service

#### scanproperties
This section is about the configuration for fetching scans from a local or a network drive 
- path: the path to your files
- port: the port to connect to the network drive. Use "0" or delete this property if the documents are stored on a local folder
- username: the username for connecting to the network share. Leave empty or delete this property if the documents are stored on a local folder
- password: the password for connecting to the network share. Leave empty or delete this property if the documents are stored on a local folder
- scannerprefix: the prefix of the filename your scanner produces. Use "*" if you want to process all files. Comes in handy if you're using several scanners and only want to process the output of of a specific subset of scanners
- filetypes: the filetypes you want to process. Use "*" to process all files
- enabled: the flag to enable or disable this service

#### archiveproperties
This section is about the configuration where your documents will be stored
- path: the path to your target documents folder
- port: the port to connect to the network drive. Use "0" or delete this property if the documents are stored on a local folder
- username: the username for connecting to the network share. Leave empty or delete this property if the documents are stored on a local folder
- password: the password for connecting to the network share. Leave empty or delete this property if the documents are stored on a local folder

#### reminderproperties
This section is about the configuration of reminders, if there is a document with due dates. Currently I only support telegram
- botname: the name of the bot
- token: the telegram-api token
- chatId: the chatId of the chat where the reminders are published to
- url: the webhook url of the api
- enabled: the flag to enable or disable this service

## DMN-Table
There is a sample dmn table provided (empty.dmn)

    | TOPIC  | SENDER  | PATH_TO_SAVE  | REMINDER  | NOTES  |
    |---|---|---|---|---|
    | - | "UNKNOWN"  | "/path/to/review/"  | true  | Unknown to unknown  |
    | "CREDITCARD"  | - | "/path/to/finance/review/"  | true  | Credit card statements to folder "review"  |
    | "OTHER"  | - | "/path/to/review/"  | true  | Unknown topics to folder "review"  |
    | - | - | "/path/to/%sender%/"  | false  | The rest will be sorted by sender  |
    