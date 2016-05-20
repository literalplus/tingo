package li.l1t.tingo.config;


/**
 * Represents a link in the top navbar.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-05-20
 */
public class NavbarLink {
    private String caption;
    private String url;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return whether this link refers to an Angular UI State
     */
    public boolean isStateRef() {
        return url.startsWith("state://");
    }

    /**
     * Returns the state reference this link refers to. If it does not
     * {@link #isStateRef() refer to a state}, the behaviour is undefined.
     *
     * @return the ui-sref value for the state this link refers to
     */
    public String getStateRef() {
        return url.replace("state://", "");
    }
}
