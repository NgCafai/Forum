package com.wujiahui.forum.dao.elasticsearch;

import com.wujiahui.forum.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by NgCafai on 2019/8/18 15:24.
 */
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {
}
