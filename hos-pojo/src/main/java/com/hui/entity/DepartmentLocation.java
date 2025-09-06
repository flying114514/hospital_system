package com.hui.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("department_locations")
public class DepartmentLocation {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @TableField("name")
    private String name;
    
    /**
     * 科室ID
     */
    @TableField("department_id")
    private Long departmentId;

    
    /**
     * 楼层
     */
    @TableField("floor")
    private String floor;
    
    /**
     * 房间号
     */
    @TableField("room_number")
    private String roomNumber;

}