package mal.coverage.viewer.model.util;

import mal.coverage.viewer.model.MalModel;
import java.io.File;
import java.util.List;

public interface ModelLoader {
	public List<MalModel> parse(File file);
}

