package io.jatinjindal.backend.store;

import io.jatinjindal.backend.dto.common.Model;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModelStore {

    private static final List<Model> models = new ArrayList<>();

    public void saveAll(List<Model> models) {
        ModelStore.models.addAll(models);
    }

    public boolean containsModel(String model) {
        return ModelStore.models.stream().anyMatch(
                m -> m.getName().equals(model)
        );
    }
}
