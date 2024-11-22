update provider_url_config set `value` = 'POST' where `group`='REJECT_OFFER' and `type`='OTHER' and `partner_id`=13;


INSERT INTO provider_template (partner_id,source,destination,`type`,`group`) VALUES
	 (13,'$.rejectReason','/reason','static','REJECT_OFFER'),
	 (13,'$.sanctionCode','/id','static','REJECT_OFFER'),
	 (13,'$.remarks.','/remarks','static','REJECT_OFFER');