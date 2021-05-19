package com.task.album;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlRootElement(name = "album")
public class Album {

	private Integer userId;

	private Integer id;

	private String title;

}
