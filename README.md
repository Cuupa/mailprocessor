# mailprocessor

![GitHub](https://img.shields.io/github/license/Cuupa/mailprocessor) ![CI](https://github.com/Cuupa/mailprocessor/workflows/CI/badge.svg) ![GitHub issues](https://img.shields.io/github/issues-raw/Cuupa/mailprocessor) ![GitHub pull request](https://img.shields.io/github/issues-pr-raw/Cuupa/mailprocessor) ![Commit activity](https://img.shields.io/github/commit-activity/m/cuupa/mailprocessor)

## Content
- [About this project](https://github.com/Cuupa/mailprocessor#about-this-project)
- [How to contribute](https://github.com/Cuupa/mailprocessor#how-to-contribute)
- [How it works](https://github.com/Cuupa/mailprocessor#how-it-works)
- [Configuration](https://github.com/Cuupa/mailprocessor#configuration)

## About this project
This is a project to automate archiving of documents.
Currently, it supports fetching from an email account or working through documents on a folder, being a local folder or on a webdav share.

This project is provided via the MIT-licence, which is free of charge.
This is a work-in-progress and done in my spare time.

## How to contribute
If you want to participate, feel free to create pull requests, fork this project, create new issues or hit me up with suggestions.
When creating an issue or a pull request, please be as detailed as possible.

"I want to participate, but I know nothing about programming 😔"
- No problem. You can contribute by providing translations for the [mappings](https://github.com/Cuupa/mailprocessor/tree/master/src/main/resources/locales) or contribute by providing feedback, make some suggestions eg.

If you think this project is awesome, you can spend me a beer or a coffee.

![BuyMeACoffee](https://img.shields.io/badge/Support%20%20me-Buy%20me%20a%20coffee-success?logo=buymeacoffee&link=https://buymeacoff.ee/Cuupa)

[Direct link](https://buymeacoff.ee/Cuupa)
## How it works
This project uses [Camunda BPMN](https://camunda.com) (or see their [github repository](https://github.com/camunda)) for orchestration  and DMN for decision tables.

You can access the Camunda Cockpit by going to http://addressofyour.server/camunda

The project is a spring boot project, so it works as a jar file as well as deployed to a tomcat/websphere/glassfish/etc application server.
It works closely with my other project called [classificator](https://github.com/Cuupa/classificator) but since it's a simple REST call, you can swap it out to your likings.

![BPMN-Process](https://github.com/Cuupa/mailprocessor/blob/master/src/main/resources/mailprocessor.png "BPMN-Process")

## Configuration
Below there's a sample configuration file in plain old json. Each section can be deactivated by simply deleting the property or using the enabled switch.
The path to the configuration is specified in the application.yml.

### Example configuration file
    [{
      "username": "username",
      "locale": "en_EN",
      "emailproperties": {
        "servername": "imap.mail.com",
        "port": 993,
        "username": "username@mail.com",
        "password": "password",
        "protocol": "imaps",
        "labels": ["inbox", "label1", "label2"],
        "markasread": true,
        "enabled": false
      },
      "scanproperties": {
        "path": "\\path\\to\\documents",
        "port": 0,
        "errorfolder": "path\\to\\error",
        "successfolder": "path\\to\\success",
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

#### emailproperties
This section is about the configuration of fetching emails. It will only process unread emails.
- servername: the servername of your mailprovider
- port: the port the mailserver provides
- username: the username for your mailaccount
- password: the password for your mailaccount. I strongly encurage you to use a one-time-password like gmail offers
- protocol: the protocol which you use to talk to your mailserver. IMAP (recommended) or POP3. IMAP differntiates between "imap" and "imaps" (TLS)
- labels: the labels or folder you want to process. Use "\*" to process all labels and sublabels
- markasread: mark the processed mails as "read"
- enabled: the flag to enable or disable this service

#### scanproperties
This section is about the configuration for fetching scans from a local or a network drive
- path: the path to your files
- port: the port to connect to the network drive. Use "0" or delete this property if the documents are stored on a local folder
- errorfolder: the folder for the not archived documents
- successfolder: the folder for the archived documents
- username: the username for connecting to the network share. Leave empty or delete this property if the documents are stored on a local folder
- password: the password for connecting to the network share. Leave empty or delete this property if the documents are stored on a local folder
- scannerprefix: the prefix of the filename your scanner produces. Use "\*" if you want to process all files. Comes in handy if you're using several scanners and only want to process the output of of a specific subset of scanners
- filetypes: the filetypes you ## Custom DMN-Table
For the routing to work, you need to add want to process. Use "\*" to process all files
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

### DMN-Table
There is a sample dmn table provided (empty.dmn) with the hit policy "First". This means the first rule matching the given criterias wins.
The desicion tables are referenced by username provided in the config above.
That means the table with the ID "john.doe" belongs to the user "john.doe" and needs to be provided under "/src/main/resources".
Feel free to change as you seem fit like adding new rules etc.

    | TOPIC         | SENDER          | PATH_TO_SAVE                        | REMINDER  | NOTES                                      |
    |---------------|-----------------|-------------------------------------|-----------|--------------------------------------------|
    |       -       | "UNKNOWN"       | "/path/to/review/"                  | true      | Unknown to "review"                        |
    |       -       | "Insurance ldt" | "/path/to/%sender%/%policyNumber%/" | false     | Insurance to the "sender/policyNumber"     |
    | "CREDITCARD"  |     -           | "/path/to/finance/review/"          | true      | Credit card statements to folder "review"  |
    | "OTHER"       |     -           | "/path/to/review/"                  | true      | Unknown topics to folder "review"          |
    |       -       |     -           | "/path/to/%sender%/"                | false     | The rest will be sorted by sender          |

The strings starting and ending with % (%sender%, %policyNumber%) will be replaced with the acutal value detected by the classificator.
