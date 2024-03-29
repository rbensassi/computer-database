package main.java.com.excilys.cdb.taglib;

import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class Paginator extends SimpleTagSupport {
    private String uri;
    private int currPage;
    private int totalPages;
    private int maxLinks = 10;

    /**
     * Obtenir le writer du jsp.
     * @return Un Writer
     */
    private Writer getWriter() {
        JspWriter out = getJspContext().getOut();
        return out;
    }

    @Override
    public void doTag() throws JspException {
        Writer out = getWriter();

        boolean lastPage = currPage == totalPages;
        int pgStart = Math.max(currPage - maxLinks / 2, 1);
        int pgEnd = pgStart + maxLinks;
        if (pgEnd > totalPages + 1) {
            int diff = pgEnd - totalPages;
            pgStart -= diff - 1;
            if (pgStart < 1) {
                pgStart = 1;
            }
            pgEnd = totalPages + 1;
        }

        try {
            out.write("<ul class=\"pagination\">");

            if (currPage > 1) {
                out.write(constructLink(currPage - 1, "<<"));
            }

            StringBuilder link;

            for (int i = pgStart; i < pgEnd; i++) {
                if (i == currPage) {
                    link = new StringBuilder("<li");
                    link.append(" class=\"").append("liStyle").append("\"").append(">");
                    link.append("<a href=\"").append(uri.replace("##", String.valueOf(i))).append("\"")
                    .append(" class=\"").append("aCurrent").append("\"").append(">").append(i).append("</a></li>");
                    out.write(link.toString());
                } else {
                    out.write(constructLink(i));
                }
            }
            if (!lastPage) {
                out.write(constructLink(currPage + 1, ">>"));
            }
            out.write("</ul>");

        } catch (java.io.IOException ex) {
            throw new JspException("Error in Paginator tag", ex);
        }
    }

    /**
     * Pour construire un lien vers une autre page.
     * @param page un entier indiquant le numero de la page
     * @return une chaine de caractere representant le html du lien genere
     */
    private String constructLink(int page) {
        return constructLink(page, String.valueOf(page));
    }

    /**
     * Constuit les balises li et a pour une page.
     * @param page un entier qui correspond au numero de page
     * @param text une chaine de caratere
     * @return Une chaine de caratere representant le html du lien genere
     */
    private String constructLink(int page, String text) {
        StringBuilder link = new StringBuilder("<li");
        link.append(" class=\"").append("liStyle").append("\"").append(">")
            .append("<a href=\"").append(uri.replace("##", String.valueOf(page))).append("\">").append(text).append("</a></li>");

        return link.toString();
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setMaxLinks(int maxLinks) {
        this.maxLinks = maxLinks;
    }
}
