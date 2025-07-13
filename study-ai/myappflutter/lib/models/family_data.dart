const Map<String, dynamic> familyData = {
  "id": "123",
  "name": "张三",
  "avatar":
      "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
  "generation": 0,
  "relationship": "self",
  "spouses": [
    {
      "id": "124",
      "name": "李四",
      "avatar":
          "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
      "relationship": "spouse",
      "generation": 0,
      "spouses": [],
      "parents": [],
    },
  ],
  "parents": [
    {
      "id": "111",
      "name": "张父",
      "avatar":
          "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
      "relationship": "parent",
      "generation": -1,
      "spouses": [
        {
          "id": "112",
          "name": "张母",
          "avatar":
              "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
          "relationship": "spouse",
          "generation": -1,
        },
      ],
      "parents": [
        {
          "id": "101",
          "name": "张祖父",
          "avatar":
              "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
          "relationship": "parent",
          "generation": -2,
        },
      ],
    },
  ],
  "children": [
    {
      "id": "125",
      "name": "张小三",
      "avatar":
          "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
      "relationship": "child",
      "generation": 1,
      "spouses": [],
      "children": [],
    },
  ],
};
