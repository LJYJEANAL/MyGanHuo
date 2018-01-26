package com.ng.ganhuoapi.data.video;

import java.util.List;

/**
 * Created by Administrator on 2018/1/2.
 */

public class CommentBean {
    private String message;
    private List<BeanData>data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BeanData> getData() {
        return data;
    }

    public void setData(List<BeanData> data) {
        this.data = data;
    }

    public static class BeanData{
        private CommentData comment;
        private int cell_type;

        public CommentData getComment() {
            return comment;
        }

        public void setComment(CommentData comment) {
            this.comment = comment;
        }

        public int getCell_type() {
            return cell_type;
        }

        public void setCell_type(int cell_type) {
            this.cell_type = cell_type;
        }
    }
}
