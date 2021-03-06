package xyz.fusheng.project.model.base;

/**
 * @FileName: Page
 * @Author: code-fusheng
 * @Date: 2021/4/21 2:09 下午
 * @Version: 1.0
 * @Description: 基础分页对象
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import xyz.fusheng.project.common.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class BasePage<T> {

    /**
     * asc 升序
     * desc 倒序
     */
    private static final String SORT_ASC = "asc";
    private static final String SORT_DESC = "desc";

    /**
     * 当前页
     */
    private Integer pageNum = 1;

    /**
     * 每页显示条数
     */
    private Integer pageSize = 10;

    /**
     * 总页数
     */
    private Integer totalPage = 0;

    /**
     * 总条数
     */
    private Integer totalCount = 0;

    /**
     * 分页起始位置
     */
    private Integer index;

    /**
     * 数据
     */
    @ApiModelProperty(hidden = true)
    private List<T> list;

    /**
     * 条件参数
     */
    private Map<String, Object> params = new HashMap<>(16);

    /**
     * 排序列
     */
    private String sortColumn;

    /**
     * 排序方式
     */
    private String sortMethod = "asc";

    /**
     * 获取当前页 头溢出 当前当前页数小于1时，赋值为1
     * @return
     */
    public Integer getPageNum(){
        if(pageNum < 1){
            return 1;
        }
        return this.pageNum;
    }

    /**
     * 获取index
     * @return
     */
    public Integer getIndex(){
        return (pageNum - 1) * pageSize;
    }

    /**
     * 设置总条数的时候计算总页数
     * @param totalCount
     */
    public void setTotalCount(Integer totalCount){
        this.totalCount = totalCount;
        this.totalPage = (int) Math.ceil(totalCount * 1.0 / pageSize);
    }

    /**
     * 设置排序方式
     */
    public void setSortMethod(String sortMethod) {
        if (StringUtils.isBlank(sortMethod)) {
            this.sortMethod = SORT_ASC;
        }
        if (sortMethod.toLowerCase().startsWith(SORT_ASC)) {
            this.sortMethod = SORT_ASC;
        } else if (sortMethod.toLowerCase().startsWith(SORT_DESC)) {
            this.sortMethod = SORT_DESC;
        } else {
            this.sortMethod = SORT_ASC;
        }
    }

    public String getSortColumn() {
        return StringUtils.isEmpty(this.sortColumn) ? "created_time" : StringUtils.upperCharToUnderLine(sortColumn);
    }

    @ApiModelProperty(hidden = true)
    public IPage<T> getPage() {
        return new Page<T>().setCurrent(this.pageNum).setSize(this.pageSize);
    }

    public BasePage(IPage iPage) {
        this.list = iPage.getRecords();
        this.totalCount = Math.toIntExact(iPage.getTotal());
        this.totalPage = Math.toIntExact(iPage.getPages());
        this.pageSize = Math.toIntExact(iPage.getSize());
    }

}

