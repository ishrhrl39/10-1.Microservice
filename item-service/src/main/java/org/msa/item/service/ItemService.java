package org.msa.item.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.msa.item.domain.Item;
import org.msa.item.dto.ItemDTO;
import org.msa.item.feign.HistoryFeignClient;
import org.msa.item.repository.ItemRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
	private final ItemRepository itemRepository;
	private final HistoryFeignClient historyFeignClient;
	
	public void insertItem(ItemDTO itemDTO, String accountId) {
		SimpleDateFormat form = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = form.format(new Date());
	
		Item item = Item.builder()
				.id(itemDTO.getId())
				.accountId(accountId)
				.name(itemDTO.getName())
				.description(itemDTO.getDescription())
				.itemType(itemDTO.getItemType())
				.count(itemDTO.getCount())
				.regDts(date)
				.updDts(date)
				.build();
		itemRepository.save(item);
		
		Map<String, Object> historyMap = new HashMap<String, Object>();
		historyMap.put("accountId", accountId);
		historyMap.put("itemId", itemDTO.getId());
		log.info("feign result = {}", historyFeignClient.saveHistory(historyMap));
	}

}
