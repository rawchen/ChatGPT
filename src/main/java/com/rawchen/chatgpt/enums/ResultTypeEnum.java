package com.rawchen.chatgpt.enums;


/**
 * 请求结果ENUM
 *
 * @author RawChen
 * @date 2022-12-24
 */
public enum ResultTypeEnum {
	ok(200), fail(400), error(500);
	private Integer typeCode;

	ResultTypeEnum(Integer code) {
		this.typeCode = code;
	}

	public Integer getTypeCode() {
		return typeCode;
	}
}
