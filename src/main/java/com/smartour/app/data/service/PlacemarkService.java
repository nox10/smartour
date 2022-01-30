package com.smartour.app.data.service;

import com.smartour.app.data.entity.map.Placemark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.smartour.app.util.PlacemarkConverter.getNormalizedPhrase;

@Service
public class PlacemarkService {

    private final PlacemarkRepository repository;

    public PlacemarkService(@Autowired PlacemarkRepository repository) {
        this.repository = repository;
    }

    public Optional<Placemark> get(Integer id) {
        return repository.findById(id);
    }

    public List<Placemark> findByPhrase(String phrase) {
        String normalizedPhrase = getNormalizedPhrase(phrase);

        return repository.findAll().stream().filter(p ->
                p.getNormalizedDescription().contains(normalizedPhrase)
                        || p.getNormalizedName().contains(normalizedPhrase)).toList();
    }

    public Placemark update(Placemark entity) {
        return repository.save(entity);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public Page<Placemark> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }
}
