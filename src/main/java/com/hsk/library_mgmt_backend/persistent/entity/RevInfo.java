package com.hsk.library_mgmt_backend.persistent.entity;

import com.hsk.library_mgmt_backend.persistent.entity.base.CustomRevisionListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "revinfo")
@RevisionEntity(CustomRevisionListener.class)
@SequenceGenerator(name = "revinfo_seq")
public class RevInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -4872732562223456299L;

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "revinfo_seq")
    @Id
    @RevisionNumber
    Long rev;

    @RevisionTimestamp
    private Long revtstmp;

    @Column(nullable = false, updatable = false)
    private String username;

    @Column(name = "user_type", nullable = false, updatable = false)
    private String userType;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) {
            return false;
        }
        Class<?> oEffectiveClass = o instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        RevInfo revInfo = (RevInfo) o;
        return getRev() != null && Objects.equals(getRev(), revInfo.getRev());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
