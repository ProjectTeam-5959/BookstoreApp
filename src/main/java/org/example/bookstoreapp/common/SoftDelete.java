package org.example.bookstoreapp.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public class SoftDelete {

    // Soft Delete용 컬럼
    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    // Soft Delete 메서드
    public void softDelete() {
        this.deleted = true;
    }

    // 복구 메서드
    public void restore() {
        this.deleted = false;
    }

    // Deleted 상태 확인 메서드
    public boolean isDeleted() {
        return this.deleted;
    }
}
