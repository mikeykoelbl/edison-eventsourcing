package de.otto.edison.eventsourcing.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.otto.edison.aws.configuration.AwsConfiguration;
import de.otto.edison.aws.s3.S3Service;
import de.otto.edison.aws.s3.configuration.S3Configuration;
import de.otto.edison.eventsourcing.s3.SnapshotWriteService;
import de.otto.edison.eventsourcing.s3.SnapshotReadService;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EventSourcingProperties.class)
@ImportAutoConfiguration({
        AwsConfiguration.class,
        S3Configuration.class,
        EventSourcingBootstrapConfiguration.class
})
@ConditionalOnProperty(
        prefix = "edison.eventsourcing",
        name = "snapshot.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class SnapshotConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SnapshotReadService snapshotService(final S3Service s3Service,
                                               final ObjectMapper objectMapper,
                                               final EventSourcingProperties eventSourcingProperties) {
        return new SnapshotReadService(s3Service, eventSourcingProperties, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public SnapshotWriteService snapshotCreationService(final S3Service s3Service,
                                                        final EventSourcingProperties eventSourcingProperties) {
        return new SnapshotWriteService(s3Service, eventSourcingProperties);
    }
}
