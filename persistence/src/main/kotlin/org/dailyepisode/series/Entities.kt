package org.dailyepisode.series

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class SeriesEntity(val name: String,
                        val url: String,
                        val description: String,
                        @Id @GeneratedValue
                        val id: Long)
