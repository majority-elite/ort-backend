package majority.elite.ort.db

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import java.sql.Timestamp
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

/*
* row 생성, 삭제, 업데이트 시간 기록 템플릿
* entity에 상속하면 해당 entity의 table에 본 column들이 추가됨
*/
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
