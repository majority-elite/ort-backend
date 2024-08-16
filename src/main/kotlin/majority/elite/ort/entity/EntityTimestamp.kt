package majority.elite.ort.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import java.sql.Timestamp
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

@MappedSuperclass
abstract class EntityTimestamp {
  @Column(name = "deleted_at", nullable = true) private var deletedAt: Timestamp? = null
  @Column(name = "created_at", updatable = false, nullable = false)
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  private lateinit var createdAt: Timestamp
  @Column(name = "updated_at", insertable = false, nullable = true)
  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  private val updateAt: Timestamp? = null
}
