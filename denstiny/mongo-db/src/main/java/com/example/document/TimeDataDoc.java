package com.example.document;

import lombok.Data;

import java.util.List;


@Data
public class TimeDataDoc {
    private String day;
    private List<String> work_time;
    private List<String> break_time;
    private String description;

}