package model;

public class HomeSetting {

    public static final String FEATURE_MODE_NEWEST = "newest";
    public static final String FEATURE_MODE_PRICE_ASC = "price_asc";
    public static final String FEATURE_MODE_PRICE_DESC = "price_desc";

    private int id;
    private String heroBadge;
    private String heroTitle;
    private String heroHighlight;
    private String heroDescription;
    private String primaryButtonText;
    private String secondaryButtonText;
    private String featuredTitle;
    private boolean showStats;
    private boolean showFilterSidebar;
    private boolean showFeaturedSection;
    private String featuredMode;
    private Integer featuredProductId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeroBadge() {
        return heroBadge;
    }

    public void setHeroBadge(String heroBadge) {
        this.heroBadge = heroBadge;
    }

    public String getHeroTitle() {
        return heroTitle;
    }

    public void setHeroTitle(String heroTitle) {
        this.heroTitle = heroTitle;
    }

    public String getHeroHighlight() {
        return heroHighlight;
    }

    public void setHeroHighlight(String heroHighlight) {
        this.heroHighlight = heroHighlight;
    }

    public String getHeroDescription() {
        return heroDescription;
    }

    public void setHeroDescription(String heroDescription) {
        this.heroDescription = heroDescription;
    }

    public String getPrimaryButtonText() {
        return primaryButtonText;
    }

    public void setPrimaryButtonText(String primaryButtonText) {
        this.primaryButtonText = primaryButtonText;
    }

    public String getSecondaryButtonText() {
        return secondaryButtonText;
    }

    public void setSecondaryButtonText(String secondaryButtonText) {
        this.secondaryButtonText = secondaryButtonText;
    }

    public String getFeaturedTitle() {
        return featuredTitle;
    }

    public void setFeaturedTitle(String featuredTitle) {
        this.featuredTitle = featuredTitle;
    }

    public boolean isShowStats() {
        return showStats;
    }

    public void setShowStats(boolean showStats) {
        this.showStats = showStats;
    }

    public boolean isShowFilterSidebar() {
        return showFilterSidebar;
    }

    public void setShowFilterSidebar(boolean showFilterSidebar) {
        this.showFilterSidebar = showFilterSidebar;
    }

    public boolean isShowFeaturedSection() {
        return showFeaturedSection;
    }

    public void setShowFeaturedSection(boolean showFeaturedSection) {
        this.showFeaturedSection = showFeaturedSection;
    }

    public String getFeaturedMode() {
        return featuredMode;
    }

    public void setFeaturedMode(String featuredMode) {
        this.featuredMode = featuredMode;
    }

    public Integer getFeaturedProductId() {
        return featuredProductId;
    }

    public void setFeaturedProductId(Integer featuredProductId) {
        this.featuredProductId = featuredProductId;
    }
}
