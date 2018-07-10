package org.dailyepisode.hello

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SeriesRepository : CrudRepository<SeriesEntity, Long> {
  fun findByName(name: String): Optional<SeriesEntity>
}

