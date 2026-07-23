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

        if (params.get("ullon") >= params.get("lrlon") || params.get("ullat") <= params.get("lrlat") ||
                params.get("ullon") > MapServer.ROOT_LRLON || params.get("lrlon") < MapServer.ROOT_ULLON ||
                params.get("ullat") < MapServer.ROOT_LRLAT || params.get("lrlat") > MapServer.ROOT_ULLAT) {

            results.put("query_success", false);
            results.put("render_grid", new String[0][0]);
            results.put("raster_ul_lon", 0.0);
            results.put("raster_ul_lat", 0.0);
            results.put("raster_lr_lon", 0.0);
            results.put("raster_lr_lat", 0.0);
            results.put("depth", 0);
            return results;
        }

        double LonDPP = (params.get("lrlon") - params.get("ullon")) / params.get("w");

        int depth = 0;
        double curLonDPP = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / (MapServer.TILE_SIZE * Math.pow(2, 7));
        while (depth < 7) {
            double cur = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / (MapServer.TILE_SIZE * Math.pow(2, depth));
            if (cur <= LonDPP) {
                curLonDPP = cur;
                break;
            }
            depth++;
        }

        int maxIndex = (int) (Math.pow(2, depth) - 1);

        double lonPerTile = curLonDPP * MapServer.TILE_SIZE;
        int ul_x = (int) ((params.get("ullon") - MapServer.ROOT_ULLON) / lonPerTile);
        int lr_x = (int) ((params.get("lrlon") - MapServer.ROOT_ULLON) / lonPerTile);

        double latPerTile = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);
        int ul_y = (int) ((MapServer.ROOT_ULLAT - params.get("ullat")) / latPerTile);
        int lr_y = (int) ((MapServer.ROOT_ULLAT - params.get("lrlat")) / latPerTile);

        ul_x = Math.max(0, Math.min(ul_x, maxIndex));
        lr_x = Math.max(0, Math.min(lr_x, maxIndex));
        ul_y = Math.max(0, Math.min(ul_y, maxIndex));
        lr_y = Math.max(0, Math.min(lr_y, maxIndex));

        String[][] images = new String[lr_y - ul_y + 1][lr_x - ul_x + 1];

        for (int j = ul_y; j <= lr_y; j++) {
            for (int i = ul_x; i <= lr_x; i++) {
                images[j - ul_y][i - ul_x] = "d" + depth + "_x" + i + "_y" + j + ".png";
            }
        }

        double ul_lon = MapServer.ROOT_ULLON + ul_x * lonPerTile;
        double ul_lat = MapServer.ROOT_ULLAT - ul_y * latPerTile;
        double lr_lon = MapServer.ROOT_ULLON + (lr_x + 1) * lonPerTile;
        double lr_lat = MapServer.ROOT_ULLAT - (lr_y + 1) * latPerTile;

        results.put("render_grid", images);
        results.put("raster_ul_lon", ul_lon);
        results.put("raster_ul_lat", ul_lat);
        results.put("raster_lr_lon", lr_lon);
        results.put("raster_lr_lat", lr_lat);
        results.put("depth", depth);
        results.put("query_success", true);

        return results;
    }
}
