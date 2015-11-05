package hu.inbuss.vehicleshop.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
public class ThymeleafLayoutInterceptor extends HandlerInterceptorAdapter {

    private static final String DEFAULT_TEMPLATE = "templates/default";
    private static final String DEFAULT_VIEW_ATTRIBUTE_NAME = "view";

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response,
            final Object handler, final ModelAndView mav) throws Exception {
        if (mav == null || !mav.hasView()) {
            return;
        }

        final String viewName = mav.getViewName();

        if (isRedirectOrForward(viewName)) {
            return;
        }

        mav.setViewName(DEFAULT_TEMPLATE);
        mav.addObject(DEFAULT_VIEW_ATTRIBUTE_NAME, viewName);
    }

    private boolean isRedirectOrForward(final String viewName) {
        return viewName.startsWith("redirect:") || viewName.startsWith("forward:");
    }
}
