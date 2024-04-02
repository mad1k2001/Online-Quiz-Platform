package com.example.onlinequizplatform.service.impl;

import com.example.onlinequizplatform.dao.OptionDao;
import com.example.onlinequizplatform.models.Option;
import com.example.onlinequizplatform.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {
    private final OptionDao optionDao;

    @Override
    public Long createOption(Option option) {
        return optionDao.createOption(option);
    }
    @Override
    public void updateOption(Option option) {
        optionDao.updateOption(option);
    }}