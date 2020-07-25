package com.cuupa.mailprocessor;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.camunda.bpm.application.PostDeploy;
import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.spring.boot.starter.SpringBootProcessApplication;

@ProcessApplication
public class MailProcessorCamundaProcessApplication extends SpringBootProcessApplication {

    private static final Log LOG = LogFactory.getLog(MailProcessorCamundaProcessApplication.class);

    @PostDeploy
    public void started() {
        printBanner();
    }

    private void printBanner() {
        String banner = "\n";
        banner += "|  __ \\                           |  ____|           (_)           \r\n"
                  + "| |__) | __ ___   ___ ___  ___ ___| |__   _ __   __ _ _ _ __   ___ \r\n"
                  + "|  ___/ '__/ _ \\ / __/ _ \\/ __/ __|  __| | '_ \\ / _` | | '_ \\ / _ \\\r\n"
                  + "| |   | | | (_) | (_|  __/\\__ \\__ \\ |____| | | | (_| | | | | |  __/\r\n"
                  + "|_|   |_|  \\___/ \\___\\___||___/___/______|_| |_|\\__, |_|_| |_|\\___|\r\n"
                  + "                                                 __/ |             \r\n"
                  + "                                                |___/              \r\n"
                  + "     _             _           _ \r\n" + "    | |           | |         | |\r\n"
                  + " ___| |_ __ _ _ __| |_ ___  __| |\r\n" + "/ __| __/ _` | '__| __/ _ \\/ _` |\r\n"
                  + "\\__ \\ || (_| | |  | ||  __/ (_| |\r\n" + "|___/\\__\\__,_|_|   \\__\\___|\\__,_|";

        LOG.debug(banner);
    }
}
