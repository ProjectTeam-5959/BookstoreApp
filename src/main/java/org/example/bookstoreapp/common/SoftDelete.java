package org.example.bookstoreapp.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class SoftDelete {

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    public void softDelete() {
        this.deleted = true;
    }
    public void restore() {
        this.deleted = false;
    }
    public boolean isDeleted() {
        return this.deleted;
    }
}
