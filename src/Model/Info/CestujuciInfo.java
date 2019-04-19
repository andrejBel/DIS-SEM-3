package Model.Info;

import GUI.TableColumnItem;
import Utils.Helper;

import java.util.Arrays;
import java.util.List;

import static GUI.TableColumnItem.IGNORE_MAX_WIDTH;

public class CestujuciInfo {

    private long _id;

    public static List<TableColumnItem<CestujuciInfo>> ATRIBUTY = Arrays.asList(
            new TableColumnItem<CestujuciInfo>(vozidloInfo -> String.valueOf(vozidloInfo._id), "ID", 50, 100)

    );


}
