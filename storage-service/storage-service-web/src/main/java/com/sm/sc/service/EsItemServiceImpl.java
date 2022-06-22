package com.sm.sc.service;

//import com.sm.sc.domain.EsItem;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.data.elasticsearch.core.query.IndexQuery;
//import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
//import org.springframework.data.elasticsearch.core.query.SearchQuery;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;

/**
 * @author lmwl
 */
//@Service
//@Slf4j
//@AllArgsConstructor
//public class EsItemServiceImpl implements EsItemService {
//    private final ElasticsearchTemplate elasticsearchTemplate;
//
//    @Override
//    public List<EsItem> select(LocalDateTime begin, LocalDateTime end) {
//        return null;
//    }
//
//    @Override
//    public int add(Collection<EsItem> items) {
//        List<IndexQuery> list = new ArrayList<>();
//        list.add(new IndexQueryBuilder().withObject(new EsItem(1001L, "title_1", "XXX1", 123.0D, LocalDateTime.now().minusHours(1), LocalDateTime.now(), "images")).build());
//        list.add(new IndexQueryBuilder().withObject(new EsItem(1002L, "title_2", "XXX2", 123.5D, LocalDateTime.now().minusHours(1), LocalDateTime.now(), "images")).build());
//        IndexQuery indexQuery = new IndexQueryBuilder().withObject(list).build();
//        elasticsearchTemplate.index(indexQuery);
//
//        return list.size();
//    }
//
//
//}
