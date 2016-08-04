package willems.bart.isitop.models;

/**
 * Created by bart on 4/08/16.
 */
public class Asset {

    private int id;
    private String assetName;
    private int assetAmount;

    public Asset ()
    {

    }
    public int getId() { return id; }
    public String getAssetName() { return assetName; }
    public int getAssetAmount() {  return assetAmount; }

    public void setId(int id) { this.id = id; }
    public void setAssetName(String assetName) {this.assetName=assetName;}
    public void setAssetAmount(int assetAmount){this.assetAmount=assetAmount;}
}
