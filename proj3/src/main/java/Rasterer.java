import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        Map<String, Object> results = new HashMap<>();
        
        if (params.get("ullon") >= params.get("lrlon")
                || params.get("ullat") <= params.get("lrlat")
                || params.get("ullon") > MapServer.ROOT_LRLON
                || params.get("lrlon") < MapServer.ROOT_ULLON
                || params.get("ullat") < MapServer.ROOT_LRLAT
                || params.get("lrlat") > MapServer.ROOT_ULLAT) {

            results.put("query_success", false);
            results.put("render_grid", new String[0][0]);
            results.put("raster_ul_lon", 0.0);
            results.put("raster_ul_lat", 0.0);
            results.put("raster_lr_lon", 0.0);
            results.put("raster_lr_lat", 0.0);
            results.put("depth", 0);
            return results;
        }

        double lonDPP = (params.get("lrlon") - params.get("ullon")) / params.get("w");

        int depth = 0;
        double curLonDPP = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON)
                / (MapServer.TILE_SIZE * Math.pow(2, 7));

        while (depth < 7) {
            double cur = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON)
                    / (MapServer.TILE_SIZE * Math.pow(2, depth));
            if (cur <= lonDPP) {
                curLonDPP = cur;
                break;
            }
            depth++;
        }

        int maxIndex = (int) (Math.pow(2, depth) - 1);

        double lonPerTile = curLonDPP * MapServer.TILE_SIZE;
        int ulX = (int) ((params.get("ullon") - MapServer.ROOT_ULLON) / lonPerTile);
        int lrX = (int) ((params.get("lrlon") - MapServer.ROOT_ULLON) / lonPerTile);

        double latPerTile = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);
        int ulY = (int) ((MapServer.ROOT_ULLAT - params.get("ullat")) / latPerTile);
        int lrY = (int) ((MapServer.ROOT_ULLAT - params.get("lrlat")) / latPerTile);

        ulX = Math.max(0, Math.min(ulX, maxIndex));
        lrX = Math.max(0, Math.min(lrX, maxIndex));
        ulY = Math.max(0, Math.min(ulY, maxIndex));
        lrY = Math.max(0, Math.min(lrY, maxIndex));

        String[][] images = new String[lrY - ulY + 1][lrX - ulX + 1];

        for (int j = ulY; j <= lrY; j++) {
            for (int i = ulX; i <= lrX; i++) {
                images[j - ulY][i - ulX] = "d" + depth + "_x" + i + "_y" + j + ".png";
            }
        }

        double ulLon = MapServer.ROOT_ULLON + ulX * lonPerTile;
        double ulLat = MapServer.ROOT_ULLAT - ulY * latPerTile;
        double lrLon = MapServer.ROOT_ULLON + (lrX + 1) * lonPerTile;
        double lrLat = MapServer.ROOT_ULLAT - (lrY + 1) * latPerTile;

        results.put("render_grid", images);
        results.put("raster_ul_lon", ulLon);
        results.put("raster_ul_lat", ulLat);
        results.put("raster_lr_lon", lrLon);
        results.put("raster_lr_lat", lrLat);
        results.put("depth", depth);
        results.put("query_success", true);

        return results;
    }
}
