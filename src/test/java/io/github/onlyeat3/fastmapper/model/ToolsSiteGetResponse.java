package io.github.onlyeat3.fastmapper.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ToolsSiteGetResponse {

    /**
     * page_info : {"total_number":"3","page":1,"page_size":100,"total_page":1}
     * list : [{"status":5,"name":"站点","siteType":"0","createTime":"2019-07-31 15:50:52","thumbnail":"https://p1-tt.byteimg.com/img/ad-tetris-site/screenshot/4da018fd7d2504dad31194b0688344a4~noop.jpeg","siteId":"6719731677658087431","pgmCount":0,"modifyTime":"2019-07-31 15:53:38"}]
     */

    private PageInfoBean pageInfo;
    private List<ListBean> list;

    @NoArgsConstructor
    @Data
    public static class PageInfoBean {
        /**
         * total_number : 3
         * page : 1
         * page_size : 100
         * total_page : 1
         */

        private String totalNumber;
        private int page;
        private int pageSize;
        private int totalPage;
    }

    @NoArgsConstructor
    @Data
    public static class ListBean {
        /**
         * status : 5
         * name : 站点
         * siteType : 0
         * createTime : 2019-07-31 15:50:52
         * thumbnail : https://p1-tt.byteimg.com/img/ad-tetris-site/screenshot/4da018fd7d2504dad31194b0688344a4~noop.jpeg
         * siteId : 6719731677658087431
         * pgmCount : 0
         * modifyTime : 2019-07-31 15:53:38
         */

        private Integer status;
        private String name;
        private String siteType;
        private String createTime;
        private String thumbnail;
        private String siteId;
        private Integer pgmCount;
        private String modifyTime;
    }
}