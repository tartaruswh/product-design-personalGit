package com.bewg.pd.baseinfo.modules.entity.enums;

/**
 * （产品成员类型枚举类）
 * 
 * @author dongbd
 * @date 2021/10/24 08:23
 **/
public enum MemberTypeEnum {
    /** 根节点 */
    ROOT("标准化产品体系", 0),
    /** 产品序列 */
    PRODUCT_SEQIENCE("产品序列", 1),
    /** 产品线 */
    PRODUCT_LINE("产品线", 2),
    /** 工艺阶段 */
    TECHNOLOGY_STAGE("工艺阶段", 3),
    /** 单体 */
    MONOMER("单体", 4),
    /** 单体类型 */
    MONOMER_TYPE("单体类型", 5);

    /** 描述 */
    private final String desc;
    /** 层级 */
    private final int level;

    MemberTypeEnum(String desc, int level) {
        this.desc = desc;
        this.level = level;
    }

    public String getDesc() {
        return desc;
    }

    public int getLevel() {
        return level;
    }

    /**
     * 获取子级产品成员类型枚举类
     * 
     * @author dongbd
     * @date 2021/10/27 18:13
     * @param memberTypeEnum
     *            产品成员类型枚举类
     * @return com.bewg.pd.baseinfo.modules.entity.enums.MemberTypeEnum
     */
    public static MemberTypeEnum getChildType(MemberTypeEnum memberTypeEnum) {
        int level = memberTypeEnum.getLevel();
        if (level == 5) {
            return null;
        }
        level++;
        for (MemberTypeEnum m : MemberTypeEnum.values()) {
            if (level == m.getLevel()) {
                return m;
            }
        }
        return null;
    }

    /**
     * 获取父级产品成员类型枚举类
     *
     * @author dongbd
     * @date 2021/10/28 16:27
     * @param memberTypeEnum
     *            产品成员类型枚举类
     * @return com.bewg.pd.baseinfo.modules.entity.enums.MemberTypeEnum
     */
    public static MemberTypeEnum getParentType(MemberTypeEnum memberTypeEnum) {
        int level = memberTypeEnum.getLevel();
        if (level == 0) {
            return null;
        }
        level--;
        for (MemberTypeEnum m : MemberTypeEnum.values()) {
            if (level == m.getLevel()) {
                return m;
            }
        }
        return null;
    }
}
