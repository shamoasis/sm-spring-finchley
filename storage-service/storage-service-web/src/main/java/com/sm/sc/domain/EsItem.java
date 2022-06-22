//package com.sm.sc.domain;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;
//
//import java.time.LocalDateTime;
//
///**
// * @author lmwl
// * // 关联的索引是item，类型是_doc，直接使用而不创建索引
// */
//@Document(indexName = "es_item", type = "_doc", createIndex = false)
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class EsItem {
//    @Id
//    private Long id;
//    /**
//     * title使用ik进行分词
//     */
//    @Field(type = FieldType.Text, analyzer = "ik_max_word")
//    private String title;
//    /**
//     * brand 不被分词
//     */
//    @Field(type = FieldType.Keyword)
//    private String brand;
//    @Field(type = FieldType.Double)
//    private Double price;
//    @Field(type = FieldType.Date)
//    private LocalDateTime timeOn;
//    @Field(type = FieldType.Date)
//    private LocalDateTime timeOff;
//    /**
//     * brand 不被分词，且不创建索引
//     */
//    @Field(index = false, type = FieldType.Keyword)
//    private String images;
//}
