package io.github.susimsek.account.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

@Cache(region = "accountCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "account", indexes = {
    @Index(name = "idx_account_customer_id", columnList = "customer_id"),
    @Index(name = "idx_account_deleted", columnList = "deleted")
})
@SQLDelete(sql = "UPDATE account SET deleted = true WHERE account_number=?")
@Where(clause = "deleted=false")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Account extends BaseEntity {

    @Id
    @Column(name = "account_number", nullable = false)
    private Long accountNumber;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "account_type", nullable = false, length = 100)
    private String accountType;

    @Column(name = "branch_address", nullable = false, length = 200)
    private String branchAddress;

    @Column(name = "communication_sw")
    private Boolean communicationSw;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = Boolean.FALSE;
}