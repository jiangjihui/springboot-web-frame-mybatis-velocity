package com.jjh.framework.swagger;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * SwaggerGroup
 *
 * @author jjh
 * @date 2021/03/11
 **/
@Data
public class SwaggerGroup {
    private String groupName;
    private List<String> packages = new ArrayList<>();
}
