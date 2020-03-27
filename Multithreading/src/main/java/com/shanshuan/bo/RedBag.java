package com.shanshuan.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by wangzifeng on 2020/3/26.
 */
@Table(name="red_envelopes")
@Getter
@Setter
@ToString
public class RedBag {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name="sum")
    private Double sum;

    @Column(name="count")
    private Integer count;

    @Column(name="version")
    private Integer version;
}
