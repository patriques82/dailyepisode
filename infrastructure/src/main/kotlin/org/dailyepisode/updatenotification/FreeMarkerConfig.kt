package org.dailyepisode.updatenotification

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean

@Configuration
class FreeMarkerConfig {

  @Bean
  fun freeMarkerConfiguration(): FreeMarkerConfigurationFactoryBean {
    val bean = FreeMarkerConfigurationFactoryBean()
    bean.setTemplateLoaderPath("/templates/")
    return bean
  }

}