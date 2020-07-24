import com.cuupa.mailprocessor.userconfiguration.*;
import com.google.gson.Gson;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

public class ConfigGenerator {

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    private final Gson gson = new Gson();

    @Test
    public void generate() {
        final ArchiveProperties
                archiveProperties =
                new ArchiveProperties().path("\\target\\path").username(USERNAME).password(PASSWORD);

        final EmailProperties
                emailProperties =
                new EmailProperties().servername("mail.provider")
                                     .username(USERNAME)
                                     .password(PASSWORD)
                                     .protocol("imap")
                                     .labels(List.of("inbox", "label1", "label2"))
                                     .enabled(false);

        final ScanProperties
                scanProperties =
                new ScanProperties().path("\\path\\to\\documents")
                                    .username(USERNAME)
                                    .password(PASSWORD)
                                    .scannerPrefix(List.of("scannerprefix"))
                                    .fileTypes(List.of("pdf"))
                                    .enabled(true);

        final ReminderProperties
                reminderProperties =
                new ReminderProperties().botname("botname")
                                        .token("token")
                                        .chatId("chatId")
                                        .url("http://url.call")
                                        .enabled(false);

        final UserConfiguration
                userConfiguration =
                new UserConfiguration().username("user.name")
                                       .locale(Locale.getDefault())
                                       .archiveProperties(archiveProperties)
                                       .emailProperties(emailProperties)
                                       .scanProperties(scanProperties)
                                       .reminderProperties(reminderProperties);

        List<UserConfiguration> config = List.of(userConfiguration);
        final String json = gson.toJson(config);
        System.out.println(json);
    }

}
