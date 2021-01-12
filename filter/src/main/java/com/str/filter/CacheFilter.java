package com.str.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebFilter(urlPatterns = "/slow/*")
public class CacheFilter implements Filter {
    // Path到byte[]的缓存
    private Map<String, byte[]> cache = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        // 获取path
        String url = req.getRequestURI();
        // 获取缓存内容
        byte[] data = this.cache.get(url);
        resp.setHeader("X-Cache-Hit", data == null ? "No" : "Yes");
        if (data == null) {
            // 缓存为找到，构造一个伪造的Response
            CachedHttpServletResponse wrapper = new CachedHttpServletResponse(resp);
            // 让下游组件写入数据到伪造的Response
            chain.doFilter(request, wrapper);
            // 从伪造的Response中读取写入的内容并放入缓存
            data = wrapper.getContent();
            cache.put(url, data);
        }
        // 写入到原始的Response
        ServletOutputStream output = resp.getOutputStream();
        output.write(data);
        output.flush();
    }
}

class CachedHttpServletResponse extends HttpServletResponseWrapper {
    private boolean open = false;   //
    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    public CachedHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    // 获取Writer
    public PrintWriter getWriter() throws IOException {
        if (open) {
            throw new IllegalStateException("Cannot re-open writer!");
        }
        open = true;
        return new PrintWriter(output, false, StandardCharsets.UTF_8);
    }

    // 获取OutputStream
    public ServletOutputStream getOutputStream() throws IOException {
        if (open) {
            throw new IllegalStateException("Cannot re-open output stream!");
        }
        open = true;
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener listener) {
            }

            // 实际写入ByteArrayOutputStream
            @Override
            public void write(int b) throws IOException {
                output.write(b);
            }
        };
    }

    // 返回写入的byte[]
    public byte[] getContent() {
        return output.toByteArray();
    }
}
