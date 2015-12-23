#pragma once
#include <assert.h>
#include <stdlib.h>

namespace Utility
{
	template <typename T> 
	class Singleton
	{
	protected:
		static T* m_pInstance;
	public:
		Singleton( void ){
//			assert( !m_pInstance );
			m_pInstance = static_cast< T* >( this );
		}
		~Singleton( void ){  
			assert( m_pInstance );  
			m_pInstance = 0;  
		}
		static T* Instance(void)
		{
			if (m_pInstance == NULL){
				m_pInstance = new T;
			}
			return m_pInstance;
		}
		static void DestoryInstance()
		{
			if (m_pInstance != NULL)
			{
				delete m_pInstance;
			}
		}
		
	};
	template<typename T> T* Singleton<T>::m_pInstance = 0;
}