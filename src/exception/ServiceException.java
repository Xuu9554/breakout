package exception;

import cn.hutool.core.util.StrUtil;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -7796605938040926930L;

    /**
     * 构造ServiceException
     *
     * @param errorMessage 报错提示语
     * @param params       填充参数
     */
    public ServiceException(String errorMessage, Object... params) {
        super(StrUtil.format(errorMessage, params));
    }

}
