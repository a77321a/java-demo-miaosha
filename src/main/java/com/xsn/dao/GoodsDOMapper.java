package com.xsn.dao;

import com.xsn.dataobject.GoodsDO;
import com.xsn.dataobject.GoodsDOExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods
     *
     * @mbg.generated Sun Feb 23 13:47:52 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods
     *
     * @mbg.generated Sun Feb 23 13:47:52 CST 2020
     */
    int insert(GoodsDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods
     *
     * @mbg.generated Sun Feb 23 13:47:52 CST 2020
     */
    int insertSelective(GoodsDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods
     *
     * @mbg.generated Sun Feb 23 13:47:52 CST 2020
     */
    List<GoodsDO> selectByExample(GoodsDOExample example);
    List<GoodsDO> selectGoodsList();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods
     *
     * @mbg.generated Sun Feb 23 13:47:52 CST 2020
     */
    GoodsDO selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods
     *
     * @mbg.generated Sun Feb 23 13:47:52 CST 2020
     */
    int updateByPrimaryKeySelective(GoodsDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods
     *
     * @mbg.generated Sun Feb 23 13:47:52 CST 2020
     */
    int updateByPrimaryKey(GoodsDO record);
    void increaseSales(@Param("id")Integer id, @Param("amount") Integer amount);
}